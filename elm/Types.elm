module Types exposing (..)

import Date exposing (Date)
import Material


freshDiaperEvent : DiaperEvent
freshDiaperEvent =
    { id = "lol"
    , attendedAt = Date.fromTime 1
    , skippedPrevious = True
    , poop = 0
    , pee = True
    , breastFeed = 0
    , bottleFeed = 0
    , sleptAt = Just <| Date.fromTime 1
    }


type TimeDelta
    = Now
    | Minus15
    | Minus30
    | Minus45
    | Minus60
    | Minus90
    | Minus120


type alias Model =
    { newEvent : DiaperEvent
    , events : List DiaperEvent
    , showNewEvent : Bool
    , neweventDeltas : NewEventDeltas
    , mdl : Material.Model
    }


type alias DiaperEvent =
    { id : String
    , attendedAt : Date
    , skippedPrevious : Bool
    , poop : Int
    , pee : Bool
    , breastFeed : Int
    , bottleFeed : Int
    , sleptAt : Maybe Date
    }


type alias NewEventDeltas =
    { attended : TimeDelta, slept : TimeDelta }


type FieldChange
    = ChangeAttended TimeDelta
    | ChangeSkippedPrevious Bool
    | ChangePoop String
    | ChangePee Bool
    | ChangeBreastFeed String
    | ChangeBottleFeed String
    | ChangeSlept TimeDelta


type Msg
    = Entry FieldChange
    | ResetNewEvent
    | CancelEvent
    | NoOp
    | Mdl (Material.Msg Msg)
