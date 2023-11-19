# Tinkoff To-Do-List REST API coursework

## Bump version
- **Patch**
> mvn build-helper:parse-version versions:set -DnewVersion=${parsedVersion.majorVersion}.${parsedVersion.minorVersion}.${parsedVersion.nextIncrementalVersion} versions:commit
- **Minor** 
>mvn build-helper:parse-version versions:set -DnewVersion=${parsedVersion.majorVersion}.${parsedVersion.nextMinorVersion}.0 versions:commit
- **Major** 
> mvn build-helper:parse-version versions:set -DnewVersion=${parsedVersion.nextMajorVersion}.0.0 versions:commit






