#!/bin/bash

# Move to project root
cd "$(dirname "$0")/.."

# Run tests and generate JaCoCo report (HTML will be in target/site/jacoco)
mvn clean test jacoco:report -Dmaven.test.failure.ignore=true

# Create reports/jacoco if it doesn't exist
mkdir -p reports/jacoco

# Copy JaCoCo HTML report to reports/jacoco
cp -r target/site/jacoco/* reports/jacoco/

# Generate Allure report in reports/allure-report
allure generate target/allure-results --clean -o reports/allure-report

echo "JaCoCo report: reports/jacoco/index.html"
echo "Allure report: reports/allure-report/index.html"