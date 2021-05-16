# Auraxis Control Center
This is the source code for the Auraxis Control Center

In order to build the application you will need to provide the following credentials. The recommended approach is to use your gradle.properties file. This is the file located in `~/.gradle/gradle.properties` **NOT** the one checked into the repo.
The following properties are required:
```
  ps2linkConsumerSecret=...
  ps2linkConsumerKey=...
  ps2linkAccessToken=...
  ps2linkAccessTokenSecret=...
  ps2linkAppCenterId=...
```

Now you can compile the app with `./gradlew auraxiscontrolcenter:release`

# Build Status

[![Build Status](https://dev.azure.com/CRamsan/AuraxisControlCenter/_apis/build/status/AuraxisControCenter?branchName=master)](https://dev.azure.com/CRamsan/AuraxisControlCenter/_build/latest?definitionId=6&branchName=master)

# Documentation
[Mocks](https://www.figma.com/files/project/30562182/Team-project?fuid=741159602862694256)

# Analytics

[Engagement](https://appcenter.ms/users/cramsan/apps/AuraxisControlCenter/analytics/overview)

[Issues](https://appcenter.ms/users/cramsan/apps/AuraxisControlCenter/crashes/errors?version=&appBuild=&period=last30Days&status=&errorType=all&sortCol=lastError&sortDir=desc)

[Events](https://appcenter.ms/users/cramsan/apps/AuraxisControlCenter/analytics/events)

[Play Stores](https://play.google.com/apps/publish/?account=6214892269219109827#StatisticsPlace:p=com.cesarandres.ps2link&statms=ALL_ACTIVE_DEVICE_EVENTS_INTERVAL&statgs=DAILY&statd=OS_VERSION&statc=true&dvals=@OVERALL@&dvals=28&dvals=29&dvals=26&dvals=24&cask=false&statdr=20200322-20200420&statcdr=20200221-20200321&grdk=@OVERALL@&bpk=3:3ef4c27cc69b19f5)

# Bug Tracker

[Github Issues](https://github.com/CRamsan/PetProject/labels/acc)

# Distribution
[Internal](https://install.appcenter.ms/users/cramsan/apps/auraxiscontrolcenter/distribution_groups/development)

[Play Store](https://play.google.com/store/apps/details?id=com.cesarandres.ps2link)

# Feedback

[Reviews](https://play.google.com/apps/publish/?account=6214892269219109827#ReviewsPlace:p=com.cesarandres.ps2link&appid=4976039285011980369)

[Ratings](https://play.google.com/apps/publish/?account=6214892269219109827#RatingsPlace:p=com.cesarandres.ps2link&appid=4976039285011980369)

## References
---

- Template: [PROJECT.md](../docs/templates/PROJECT.md).