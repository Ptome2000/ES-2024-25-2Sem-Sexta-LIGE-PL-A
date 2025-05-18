# ES-2024-25-2Sem-Sexta-LIGE-PL-A

<h3> School project related to the curricular unit of "Software Engineering" composed of the following members: </h3>

<table>
  <tr>
    <th> Name </th>
    <th> Student ID </th>
    <th> GitHub Username </th>
  </tr>
  <tr>
    <td> Alexandre Venâncio </td>
    <td> 110660 </td>
    <td> alexandrevenancio21 </td>
  </tr>
  <tr>
    <td> Filipa Romão </td>
    <td> 100744 </td>
    <td> pipsromao </td>
  </tr>
  <tr>
    <td> Lucas Jardim </td>
    <td> 111353  </td>
    <td> LucasJardim2004 </td>
  </tr>
    <tr>
      <td> Pedro Tomé </td>
        <td> 106085 </td>
        <td> ptome2000 </td>
    </tr>
</table>

<h3> Functionalities </h3>
<h4> Implemented: </h4>
<ul>
  <li> CSV importation </li>
  <li> Properties graph showed </li>
  <li> Same owner grouping </li>
  <li> Mean property area calculated by parish, municipality and district </li>
  <li> Mean property area calculated by parish, municipality and district with same owner properties grouped</li>
  <li> Changes suggestions filtered by area </li>
  <li> Changes suggestions filtered by tourism score (tourists checkpoints in the area) and urbanization </li>
  <li>Implementation of test coverage and test results reports </li>
  <li>implementation of github actions to automatically run the project tests</li>

</ul>

<h4> Not Implemented: </h4>
<ul>
<li>Color code for each region not implemented</li>
</ul>

<h3> How to run the project </h3>
<p>Run main class and import CSV in "CSV import" button</p>

<h3> How to test the project and generate reports</h3>
<ul> With maven use the following commands
<li>mvn clean test</li>
<li>mvn jacoco:report</li>
<li>mvn allure:report</li>

</ul>
<p>Another way to generate reports is by using script "generate-reports" inside scripts folder</p>

<h3> Errors / Bugs </h3>
<ul>
<li>Avoid 2 equal properties to be insered into graph</li>
</ul>


