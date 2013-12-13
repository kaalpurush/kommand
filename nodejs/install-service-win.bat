nssm.exe install kommand "C:\Program Files\nodejs\node.exe" "%~dp0kommand.js"
reg add "HKLM\SYSTEM\CurrentControlSet\services\kommand\Parameters" /f /v "AppDirectory" /t REG_EXPAND_SZ /d %~dp0
net start kommand