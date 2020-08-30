# Zoe

Personal project using the "My Renault" API to create a small, simple app to view and control your Renault Zoe (mainly the Renault Zoe ZE50 R110/R135). Work in progress...

Current version is available as an internal test release on the play store. Contact me at weisslinus@gmail.com to get access

## Extracting of API Data

If you also want to use the Renault API or you want to contribute to this project you can do the following:

- Root your device
- Install/Flash Magisk
- Install this Magisk module: https://github.com/NVISO-BE/MagiskTrustUserCerts#:~:text=Magisk%20Trust%20User%20Certs,property%20to%20an%20application's%20manifest.
- Install Burp-Suite on your PC and setup a proxy
- Install Burp-Suite Certificate on your phone
- Follow this guide to disable ssl pinning with Frida: https://www.kuketz-blog.de/android-tls-verifikation-und-certificate-pinning-umgehen/
- You should now be able to sniff the Apps data in Burp-Suite