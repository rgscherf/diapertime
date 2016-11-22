var showingNewEvent = false; // see click handler for #addEvent
var FALSE_VAL_OPACITY = 0.68; // based on google mdl guidelines for null value opacity

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
  var date = moment.unix(timeObj.$date);
  return date.format("MM/DD hh:mma");
}

function stringifyBool(b) {
  // turn bool into check or X fontawesome icon for rendering
  // note opacity change based on truthiness of b
  return b ? 
    '<i class="fa fa-check" style="opacity:1;" aria-hidden="true"></i>' : 
    '<i class="fa fa-times" style="opacity:' + FALSE_VAL_OPACITY + ';" aria-hidden="true"></i>';
}

function stringifyPoop(i) {
  // render `i` icons to signify amount of poop
  // note opacity change for `i` values of 0
  var ret = "";
  if (i === 0) {
    ret = '<i class="fa fa-times" style="opacity:' + FALSE_VAL_OPACITY + ';" aria-hidden="true"></i>';
  } else {
    for (var n = 0; n < i; n++) {
      ret += '<i class="fa fa-leaf" aria-hidden="true"></i>';
    }
  }
  return ret;
}

function roundToTen(i) {
  // round bottle feed mL to nearest 10, then display
  var bottleVolume = Math.ceil(i / 10) * 10;
  return '<div style="text-align: right;">' + bottleVolume + '</div>';
}


function makeRow(tableElement, event) {
  // abstracting html string manipulation for append() calls below
  function wraptd(label, cont) {
    return ("<td data-label='" + label + "'>" + cont + "</td>");
  }
  var newRow = $('<tr>'); // $('<>') will add matching close tag after appended tags
  newRow.append(wraptd("Attended", stringifyTime(event.attendedAt)));
  newRow.append(wraptd("Pee", stringifyBool(event.pee)));
  newRow.append(wraptd("Poop", stringifyPoop(event.poop)));
  newRow.append(wraptd("Bottle", roundToTen(event.bottleFeed)));
  newRow.append(wraptd("Slept", stringifyTime(event.sleptAt)));
  tableElement.append(newRow);
}

