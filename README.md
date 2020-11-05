# Zoe

[![Build Status](https://app.bitrise.io/app/88e7234ef3aaac97/status.svg?token=ZOZxn0aiS6PKAMWDrdjS6Q&branch=master)](https://app.bitrise.io/app/88e7234ef3aaac97)

Personal project using the "My Renault" API to create a small, simple app to view and control your Renault Zoe (mainly the Renault Zoe ZE50 R110/R135). Work in progress...

Current version is available as an internal test release on the play store. Contact me at weisslinus@gmail.com to get access

## Features

- Home Screen Widget
- Home Screen Shortcuts
- Charge Tracking
- De-Charge Tracking
- Start HVAC
- Charge Status
- Vehicle Details
- Location
- Charge planning
- HVAC planning
- Appointments list
- Contracts list

## Extracting of API Data

If you also want to use the Renault API or you want to contribute to this project you can do the following:

- Root your device
- Install/Flash Magisk
- Install this Magisk module: https://github.com/NVISO-BE/MagiskTrustUserCerts
- Install Burp-Suite on your PC and setup a proxy
- Install Burp-Suite Certificate on your phone
- Follow this guide to disable ssl pinning with Frida: https://www.kuketz-blog.de/android-tls-verifikation-und-certificate-pinning-umgehen/
- You should now be able to sniff the Apps data in Burp-Suite
