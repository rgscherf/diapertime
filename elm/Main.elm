module Main exposing (..)

import Html.App exposing (programWithFlags)
import Html exposing (..)
import Html.Attributes exposing (..)
import Date exposing (Date)


type alias Model =
    { newEvent : DiaperEvent
    , events : List DiaperEvent
    }


type alias DiaperEvent =
    { id : Int
    , created_at : Date
    , skipped_previous_feed : Maybe Bool
    , poop : Maybe Bool
    , pee : Maybe Bool
    , breast_feed : Maybe Int
    , bottle_geed : Maybe Int
    , attended_at : Maybe Date
    , slept_at : Maybe Date
    }


view : Model -> Html a
view model =
    div [] []
