@echo off
REM Move to project root
cd /d "%~dp0\.."

REM Run tests and generate JaCoCo report (HTML will be in target\site\jacoco)
mvn clean test jacoco:report -Dmaven.test.failure.ignore=true

REM Create reports\jacoco if it doesn't exist
if not exist reports\jacoco mkdir reports\jacoco

REM Copy JaCoCo HTML report to reports\jacoco
xcopy /E /Y target\site\jacoco\* reports\jacoco\

REM Generate Allure report in reports\allure-report
allure generate target\allure-results --clean -o reports\allure-report

echo JaCoCo report: reports\jacoco\index.html
echo Allure report: reports\allure-report\index.html
pause