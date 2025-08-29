## Overview

- project with a kotlin dsl for using Apache Echarts in kotlin-js projects
- dsl is in the kotlin-echarts-dsl project
- demo contains a small web project for showing some charts
- the DSL is based on JsonDsl, which uses delegates around Map to do its thing

## Build

- `gradle build`

## Test

- use kotest-assertions
- use kotlin-testing @Test annotations

## Documentation

- This project uses kotlin4example; the README.md is generated from the jvmTests for the kotlin-echarts-dsl module. 
- Don't modify the README.md directly but the source code for the test that generates the README.md