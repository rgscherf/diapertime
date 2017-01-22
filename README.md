# Diaper Time

Diaper Time represents each period of your baby's wakefulness as one event. Enter the important information for that event and instantly see how it compares to historical data.

I built Diaper Time because nothing else provided a quick, elegant way to track my baby's I/O.

[http://diaperti.me](http://diaperti.me)

# TODO

## Features - Quick

- Render summary information
- Save new user events to db
- Button to toggle percentile info
- Offer summary choice of last-24-hours or since-midnight
- New event feed defaults to avg of last X feeds

## Features - Not Quick

- Random baby name for /random
- Stretch: feed unit? (ml | oz | minutes)

## Bugs

- Fix display of "0th percentile"
- new event placeholder in CLJS must have random :id field
- ERROR 400 upon adding more than 1 new event per page visit?
  - This happens when submitting an event with :attended before the most recent :slept.
  - Just need to add an :on-error handler to the submit POST.

# DONE!
- Login/registration text fields max width 90%, for phones
- Occasional misrender of minutes/seconds (ex 2:90)
