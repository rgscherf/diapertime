////////
// STATE
////////

var showingNewEvent = false; // see click handler for #addEvent
var FALSE_VAL_OPACITY = 0.38; // based on google mdl guidelines for null value opacity

// state container for the new diaper event element
// this object will be modified in event handlers
// on the `new diaper event` component,
// then POSTed to the server
var newEventState = {
  attended: 0,
  pee: false,
  poop: 0,
  bottle: 0,
  slept: 0
};

// yes, this is the best way to copy a JS object
var defaultEventState = JSON.parse(JSON.stringify(newEventState));


//////////////////////////
// JQUERY DOM MANIPULATION
//////////////////////////

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

  setupNewEntryInput();
});


///////////////////////////
// RENDERING TABLE ELEMENTS
///////////////////////////

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
}

function stringifyGroupOfIcons(fontAwesomeClass, fieldMax, cellInput) {
  // return an HTML string consisting of:
  // given font awesome icon * given level for row
  // concatted with greyed-out icons up to max level for field

  // if cellInput === fieldMax: will display max num of icons, all opacity 1
  // if cellInput === 0: will display max num of icons, all greyed out

  var returnString = "";
  for (var filledIconCount = 0; filledIconCount<cellInput; filledIconCount++) {
    returnString += '<i class="' + fontAwesomeClass + '" aria-hidden="true"></i>';
  }
  for (var emptyIconCount = cellInput; emptyIconCount<fieldMax; emptyIconCount++) {
    returnString += '<i class="' + fontAwesomeClass + '" style="opacity:' + FALSE_VAL_OPACITY + ';" aria-hidden="true"></i>';
  }
  return returnString;
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
  newRow.append(wraptd("Poop", stringifyGroupOfIcons("fa fa-leaf", 3, event.poop)));
  newRow.append(wraptd("Bottle", stringifyGroupOfIcons("fa fa-coffee", 5, event.poop)));
  newRow.append(wraptd("Slept", stringifyTime(event.sleptAt)));
  tableElement.append(newRow);
}

///////////////////////////
// NEW EVENT INPUT ELEMENTS
///////////////////////////

function makeTouchBox(boxId, buttonWidth, boxText) {
  return ('<div> <button class="smallInput" style="width:' + buttonWidth + 'px" id=' + boxId + '>' + boxText + '</button></div>');
}

function setupNewEntryInput() {
  var newAttended = $('<div class="newInputFlex">');
  newAttended.append(makeTouchBox("a1", 80, "Earlier"))
    .append('<div>:30 ago</div>')
    .append(makeTouchBox("a2", 80, "Later"));
  $('#newEntryAttended').append(newAttended);

  var newPeed = $('<div class="newInputFlex">');
  newPeed.append(makeTouchBox("peeTouchBox", 80, "Peed?"))
         .append('<div>Yes</div>');
  $('#newEntryPee').append(newPeed);

  var newPoop = $('<div class="newInputFlex">');
  newPoop.append(makeTouchBox("poopinput", 80, "Less"))
          .append('<div>Scant</div>')
          .append(makeTouchBox("poopinput", 80, "More"));
  $('#newEntryPoop').append(newPoop);

  var newBottle = $('<div class="newInputFlex">');
  newBottle.append(makeTouchBox("a1", 80, "Less"))
           .append('<div>100 mL</div>')
           .append(makeTouchBox("a1", 80, "More"));
  $('#newEntryBottle').append(newBottle);
  $('#newEntrySlept').append(makeTouchBox("a1", 80, "Earlier"));
  $('#newEntrySlept').append('<div>:30 ago</div>');
  $('#newEntrySlept').append(makeTouchBox("a2", 80, "Later"));
}

