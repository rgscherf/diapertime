# Diaper Time

Diaper Time represents each period of your baby's wakefulness as one event. Enter the important information for that event and instantly see how it compares to historical data.

I built Diaper Time because nothing else provided a quick, elegant way to track my baby's I/O.

[http://diaperti.me](http://diaperti.me)

# Bugs
- Signup validation for duplicate email addresses
  - should not be allowed
  - should display useful error message to user
- Is summary info calculating start/end bounds correctly?
  - May not be taking time zone into account

# TODO

## Features - Quick

- events/persist-event! should not return user map
  - move to separate function.
- New event feed defaults to avg of last X feeds
- 404 page
- Loading screen needs spinner (and banner same size as landing page)
- Orientation information displayed when user table is empty

## Features - Not Quick

- Random baby name for /random
  - Use https://randomuser.me/api
- Stretch: feed unit? (ml | oz | minutes)
- Button to toggle percentile info
