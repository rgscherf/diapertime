# Diaper Time

Diaper Time represents each period of your baby's wakefulness as one event. Enter the important information for that event and instantly see how it compares to historical data.

I built Diaper Time because nothing else provided a quick, elegant way to track my baby's I/O.

[http://diaperti.me](http://diaperti.me)

# Bugs
- Signup validation for duplicate email addresses
  - should not be allowed
  - should display useful error message to user
- Cannot display new event input on empty events list
  - "Error rendering component (in clj_diaper.core.current_page > view-diaper-events > templates.events_table.render_events_table > templates.input_row.render_input_row)"
  - "Uncaught Error: Assert failed: (seq s)"
- Is summary info calculating start/end bounds correctly?
  - May not be taking time zone into account

# TODO

## Features - Quick

- events/persist-event! should not return user map
  - move to separate function.
- New event feed defaults to avg of last X feeds
- Add landing page copy with my twitter and github
- 404 page
- Loading screen needs spinner (and banner same size as landing page)

## Features - Not Quick

- Random baby name for /random
  - Use https://randomuser.me/api
- Stretch: feed unit? (ml | oz | minutes)
- Button to toggle percentile info
