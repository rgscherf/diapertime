module Types exposing (..)

import Date exposing (Date)
import Material


type alias Model =
    { newEvent : Maybe DiaperEvent
    , events : List DiaperEvent
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
    | Mdl (Material.Msg Msg)
