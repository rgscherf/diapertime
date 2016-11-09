module Types exposing (..)

import Date exposing (Date)


type alias Model =
    { newEvent : Maybe DiaperEvent
    , events : List DiaperEvent
    }


type alias DiaperEvent =
    { id : String
    , attendedAt : Date
    , skippedPrevious : Bool
    , poop : Int
    , pee : Bool
    , breastFeed : Int
    , bottleFeed : Int
    , slept_at : Maybe Date
    }


type FieldChange
    = ChangeAttended Date
    | ChangeSkippedPrevious Bool
    | ChangePoop Bool
    | ChangePee Bool
    | ChangeBreastFeed Int
    | ChangeBottleFeed Int
    | ChangeSlept Date


type Msg
    = Entry FieldChange
    | NoOp
