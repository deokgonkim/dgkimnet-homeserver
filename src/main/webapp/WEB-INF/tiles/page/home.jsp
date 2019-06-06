<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript" src="//cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.min.js"></script>

<ul>
    <li><spring:message code="home.currenttemperature" /> : <span id="temperature">${current["temperature"]} *C (${current["temperatureDate"]})</span></li>
    <li><spring:message code="home.currenthumidity" /> : <span id="humidity">${current["humidity"]} % (${current["humidityDate"]})</span></li>
</ul>

<div class="card mb-3">
    <div class="card-header">
        <i class="fa fa-area-chart"></i> <spring:message code="home.temperaturechart" />
    </div>
    <div class="card-body">
        <canvas id="tempChart" width="100%" height="30"></canvas>
    </div>
</div>

<div class="card mb-3">
    <div class="card-header">
        <i class="fa fa-area-chart"></i> <spring:message code="home.humiditychart" />
    </div>
    <div class="card-body">
        <canvas id="humiChart" width="100%" height="30"></canvas>
    </div>
</div>


<script type="text/javascript">
$(document).ready(function() {
    
    
    $.ajax({
        url: '<c:url value="/data/home.RPi.DHT11/temperature/recent" />',
        success: function(data, textStatus, xhr) {
            var newData = {'xAxis': [], 'yAxis': []};
            data.forEach(function(data, index) {
                
                newData.xAxis.push(to_hhmm(new Date(data.datetime)));
                newData.yAxis.push(Number(data.value));
            });
            
            chart = createChart('tempChart', newData);
            chart.data.datasets.push({
                label: 'temperature',
                borderColor: '#ffbc08',
                data: newData.yAxis
            });
            chart.update();
        },
        error: function(xhr, textStatus, errorThrown) {
            console.log('error');
            console.log(xhr);
            console.log(textStatus);
            console.log(errorThrown);
            console.log(data);
        }
    });
    
    $.ajax({
        url: '<c:url value="/data/home.RPi.DHT11/humidity/recent" />',
        success: function(data, textStatus, xhr) {
            
            var newData = {'xAxis': [], 'yAxis': []};
            data.forEach(function(data, index) {
                newData.xAxis.push(to_hhmm(new Date(data.datetime)));
                //newData.xAxis.push(new Date(data.datetime));
                newData.yAxis.push(Number(data.value));
                //newData.yAxis[1].push(Number(data.value)-3);
            });
            
            chart = createChart('humiChart', newData)
            chart.data.datasets.push({
                label: 'Humidity',
                borderColor: '#1878f0',
                data: newData.yAxis
            });
            chart.update();
        },
        error: function(xhr, textStatus, errorThrown) {
            console.log('error');
            console.log(xhr);
            console.log(textStatus);
            console.log(errorThrown);
            console.log(data);
        }
    });
});

function createChart(elementId, data) {
    var ctx = document.getElementById(elementId).getContext('2d');
    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.xAxis,
        },
        options: {
            scales: {
                xAxes: [{
                    ticks: {
                      autoSkip: true,
                      maxTicksLimit: 24
                    }
                }]
            }
        }
    });
    return chart;
}

function to_hhmm(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return hours + '' + minutes;
}

</script>