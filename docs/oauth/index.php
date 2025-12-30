<?php
$code  = $_GET['code']  ?? '';
$state = $_GET['state'] ?? '';

$deeplink = 'info.metadude.android.congress.schedule://oauth'
          . '?code=' . urlencode($code)
          . '&state=' . urlencode($state);
$htmlUrl = htmlspecialchars($deeplink, ENT_QUOTES, 'UTF-8');          
?>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Eventfahrplan &ndash; Hub Authentifizierung</title>
    <meta charset="utf-8">
    <meta http-equiv="refresh" content="0; URL='<?= $htmlUrl ?>'">
</head>
<body>
    <h1>Authentifizierung erfolgreich</h1>
    <p><a href="<?= $htmlUrl ?>" title="manuelle Weiterleitung" aria-label="manuelle Weiterleitung">Zurück zu der App (info.metadude.android.congress.schedule://oauth).</a></p>
</body>
</html>
