<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><!DOCTYPE html>
<div id="selectBox" ng-controller="RawCtrl">
  <h3>Delete Run</h3>

  Run: <select ng-model="run">
    <option value="">[-Select-]</option>
    <c:forEach var="run" items="${runs}">
      <option value="<c:out value="${run.getId()}"/>">
        <c:out value="${run.getRunName()}" />
      </option>
    </c:forEach>
  </select>
  <button class="btn" ng-click="deleteRun()">Delete</button>
</div>
