# Playwright Tests for a Flow-Hilla Hybrid project

This project has been downloaded from http://start.vaadin.com after selecting some views in Flow and Hilla configured with access control.

Then I have added UI tests for all the views using Playwright library.

For Flow views Playwright java API is used, whereas for Hilla views it is used the Playwright typescript API.

NOTE: For the initial generation of the tests the ui-test-generator plugin was used, although most of the test needed to be re-written during the hackathon

## Test location

- Flow IT Java Tests: [src/test/java/es/manolo/hackathon244/views](src/test/java/es/manolo/hackathon244/views)
- Hilla IT Typescript Tests: [src/test/frontend/views](src/test/frontend/views)

## Other interesting stuff
- [playwright.config.ts](playwright.config.ts)
- [pom.xml](pom.xml#L190)
- [.github/workflows/validate.yml](.github/workflows/validate.yml)

## Running Tests
You can run both: Flow and Hilla tests by executing:

```
mvn verify -Pit,production
```

For running tests in headed mode run:

```
mvn verify -Pit,production -Dheadless=false
```


If you prefer run only Hilla tests, just execute:

```
npm test
```

## Issues discovered
- [ ] https://github.com/vaadin/flow/issues/19450
- [ ] https://github.com/vaadin/ui-test-generator/issues/59
