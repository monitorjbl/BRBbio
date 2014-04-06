<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="container" ng-controller="DisplayController">
    <div id="selectBox">
        <h3>View Viability Data</h3>

        <table>
            <tr>
                <td>Run</td>
                <td><select ng-model="run" ng-change="getViabilityControls()">
                    <option value="-1">[-Select-]</option>
                    <c:forEach var="run" items="${runs}">
                        <option value="<c:out value="${run.getId()}"/>">
                            <c:out value="${run.getRunName()}"/>
                        </option>
                    </c:forEach>
                </select><a href="b/getViabilityDataExcel?runId={{run}}&func={{encodeUrl(func)}}"><img
                        src="assets/img/excel.png"></a><a
                        href="b/getViabilityDataTsv?runId={{run}}&func={{encodeUrl(func)}}"><img
                        src="assets/img/tsv.png"></a></td>
            </tr>

            <tr>
                <td>Formula</td>
                <td><textarea ng-model="func" ng-init="func = 'rawData/AVG(negativecontrol)'"></textarea></td>
            </tr>

            <tr>
                <td>
                    <button class="btn" ng-click="getViabilityData()">Process</button>
                </td>
                <td><img class="loading" src="assets/img/loader.gif" ng-show="loading"/><span class="failure">{{loadError}}</span>
                </td>
            </tr>
        </table>
    </div>

    <div id="legend">
        <h4>Information</h4>
        Cell Viability allows you to normalize the viability upload (linked or independent) by applying any basic
        Excel-style formula using the available controls and available functions for the run you
        have selected in the formula box.
        <div style="padding-left: 5px;">
            <dl>
                <dt>Available functions</dt>
                <dd>AVG(): Average of field</dd>
                <dd>STD(): Standard deviation of field</dd>
                <dd>MIN(): Lowest value of field</dd>
                <dd>MAX(): Largest value of field</dd>
            </dl>
            <dl>
                <dt>Available fields</dt>
                <dd>rawData: value of the Data column of the cytotoxicity spreadsheet</dd>
            </dl>
            <dl>
                <dt>Available controls</dt>
                <dd ng-repeat="control in controls">{{control}}</dd>
            </dl>
        </div>
    </div>

    <br/>

    <div class="row">
        <div id="processed" class="span6">
            <table ng-show="showingData" class="table table-striped">
                <thead>
                <tr>
                    <th>Plate ID</th>
                    <th>Entrez Gene ID</th>
                    <th>Gene Symbol</th>
                    <th>Viability</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="row in rows">
                    <td>{{row.plate}}</td>
                    <td>{{row.geneId}}</td>
                    <td>{{row.geneSymbol}}</td>
                    <td>{{row.data}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>