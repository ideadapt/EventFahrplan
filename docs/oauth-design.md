# OAuth Integration


## Context 

- At Chaos Communication Congress the Hub backend is used - maintained by the Hub team.
- A new API version 2 to access resources of the backend became available throughout 2025, before 39C3. The API offers OAuth for third party clients. Docs at:
  - https://api.events.ccc.de/congress/2025/v2/docs
  - https://api.events.ccc.de/congress/2025/.well-known/openid-configuration
- The HTML frontend of the Hub allows logged-in users to favor sessions, stored in the user profile in the Hub backend.
- The EventFahrplan app allows unauthenticated users to favor sessions, stored locally on the device only.
- The first goal is to import Hub-favored sessions into the app. The Hub API would also allow to modify favorites.


## Why OAuth?
Hub offers two possible authentication mechanisms: OAuth, User API token.

OAuth is well known to users. They just have to login as usual on the Hub Website and then authorize the app request. Implementing OAuth is not simple though.

User API tokens are simple to implement, but cumbersome for the user. Users would have to manually create a token in the Hub profile, then copy & paste the token in the app. Also whenever the token expires, this process has to be repeated.


## Test cases
TODO: define expected outcomes 

- No token - user has never authenticated the app before.
- Expired token - the token stored on the device has expired. The app knows this based on the stored token metadata.
- Revoked token - Requests to protected resources respond with 401 Unauthorized.
- OAuth APIs (/token, /authorize) time out.
- OAuth APIs (/token, /authorize) return 400 Bad Request or 401 Unauthorized.
- User cancels authorization request.
- User initiates and approves a second authorization request, while the first one is not approved. Only then he approves (or cancels) the first authorization request.


## Design Decisions

Decisions should follow best practices and recommendations, as documented in:

- https://oauth.net/2/native-apps/
- https://developer.android.com/privacy-and-security/security-tips


### What OAuth flow?
We use "Authorization code flow with PKCS". It adds cryptographic verification to the Authorization code flow.

"Authorization code flow with device code" would be another possibility, but is not required, since the app is not primarily designed for dual device usage (e.g. Screen Mirroring).

### How to integrate in Android platform?
We decided to implement the OAuth flow by our own, without any specialized library.

Considered libraries:

[AppAuth](https://github.com/openid/AppAuth-Android/): Unmaintained

[Locksmith](https://lokksmith.dev/): Not major yet, promising though.

### Custom Tabs, AuthTab, native Tabs, WebViews?
We use native Tabs to start with. It is simple to implement, works on any device with any browser. We might switch to Custom Tags later.

[Auth Tabs](https://developer.chrome.com/docs/android/custom-tabs/guide-auth-tab): Works only in Chrome browsers, since 132 (Jan 2025).

[Custom Tabs](https://developer.android.com/develop/ui/views/layout/webapps/overview-of-android-custom-tabs): Not all browser support it. To improve privacy, [Ephemeral Custom Tabs](https://developer.chrome.com/docs/android/custom-tabs/guide-ephemeral-tab) (privat tab) should be used.

[WebViews](https://developer.android.com/reference/android/webkit/WebView): The oldest, approach. [Not recommended](https://developers.googleblog.com/modernizing-oauth-interactions-in-native-apps-for-better-usability-and-security/) anymore for various [security reasons](https://www.oauth.com/oauth2-servers/mobile-and-native-apps/security-considerations/).

### OAuth client_secret storage at rest
To refresh an `access_token`, the `client_secret` is required by the current AllAuth configuration. This is a static, confidential security credential already known at build time. It must be protected from unauthorized access. Only the app should be able to read it.

TODO No solution approach yet. Consider https://developer.okta.com/blog/2019/01/22/oauth-api-keys-arent-safe-in-mobile-apps.

TODO OAuth only requires this parameter if it was issued (if I understand https://datatracker.ietf.org/doc/html/rfc6749#section-6 correctly). So: Could the Hub team create an API client for us, that has no client_secret?
TODO PKCE reduces the risk of client_secret leakage because it adds cryptographic verification, but then, why have a client_secret parameter at all?

### OAuth refresh_token storage at rest
To refresh an `access_token`, the corresponding `refresh_token` is required. This is a dynamic, confidential security credential known at runtime time as a result of a successful authorization. It must be protected from unauthorized access. Only the app should be able to read it.

TODO No solution approach yet. Consider https://developer.okta.com/blog/2019/01/22/oauth-api-keys-arent-safe-in-mobile-apps. 

### OAuth code_verifier storage at runtime
To retrieve an authorization code, the corresponding `code_verifier` is required. This is a dynamic, confidential security credential generated at runtime time. It must never be stored or leave the device. Only the app should be able to read it.

We store this information only in memory. As a consequence: If the app crashes before the `access_token` is retrieved, the authorization process has to start from the beginning.

### Custom scheme or AppLink?
As soon as the user has approved the authorization request in the browser, the device needs to show the app. To do so, the browser has to redirect to a configured `redirect_uri`. This URI can either be a custom scheme (e.g. `myapp://open`) or an AppLink, which is a regular website URL (e.g. https://my.app/oauth) with some server side cryptographic verification.

We decided to use a custom scheme, because it does not require server side security configuration. It requires some server side configuration though, see below.


## Implementation Details

### Webserver
The Hub backend uses [Django AllAuth](https://docs.allauth.org) plugin for OAuth configuration. This does not allow a custom scheme `redirect_uri`, it requires a `https://` schema.
We have thus configured a `redirect_uri` that points to a webserver we own (first redirect from Hub to "us"), and that webserver serves a webpage that then does a client side redirect (second redirect) to the custom scheme URI. This second redirect will trigger the device to show the app.

If we instead would do a server side redirect (instead of sending a webpage), the custom scheme URI in the second redirect does not trigger, i.e. the app is not shown.
The client side redirect does not require javascript, just HTML generated and validated on the server side with PHP.

TODO sequence diagram

### Testing

To mock Hub API responses you can use the docker image in ./oauth.

1. Build the image
2. Start the container
3. Expose the container port to the internet. Use e.g. tunnelmole. 
4. Then configure this public URL as Hub API root in the App.