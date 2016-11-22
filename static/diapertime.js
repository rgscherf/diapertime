var showingNewEvent = false; // see click handler for #addEvent

$().ready(function() {
  
  // click handler for 'add new event' button.
  // toggles event row for data entry
  $("#addEvent").click(function() {
    if (showingNewEvent) {
      $("#newEntry").hide();
    } else {
      $("#newEntry").show();
    }
    showingNewEvent = !showingNewEvent;
  });
  
  // dynamic table based on diaperEvents object array
  diaperEvents.forEach(function(elem) {
    makeRow($("#mainTable"), elem);
  });
});

function stringifyTime(timeObj) {
  var date = moment.unix(timeObj["$date"]);
  return date.format("MM/DD hh:mma");
}

function stringifyBool(b) {
  return b ? 
    '<i class="fa fa-check" aria-hidden="true"></i>' : 
    '<i class="fa fa-times" aria-hidden="true"></i>';
}

function stringifyPoop(i) {
  var ret = "";
  if (i === 0) {
    ret = '<i class="fa fa-times" aria-hidden="true"></i>';
  } else {
    for (var n = 0; n < i; n++) {
      ret += '<i class="fa fa-leaf" aria-hidden="true"></i>';
    }
  }
  return ret;
}

function roundToTen(i) {
  var bottleVolume = Math.ceil(i / 10) * 10;
  return '<div style="text-align: right;">' + bottleVolume + '</div>';
}


function makeRow(tableElement, event) {
  // abstracting html string manipulation for append() calls below
  function wraptd(label, cont) {
    return ("<td data-label='" + label + "'>" + cont + "</td>");
  }
  var newRow = $('<tr>');
  newRow.append(wraptd("Attended", stringifyTime(event.attendedAt)));
  newRow.append(wraptd("Pee", stringifyBool(event.pee)));
  newRow.append(wraptd("Poop", stringifyPoop(event.poop)));
  newRow.append(wraptd("Bottle", roundToTen(event.bottleFeed)));
  newRow.append(wraptd("Slept", stringifyTime(event.sleptAt)));
  tableElement.append(newRow);
}

