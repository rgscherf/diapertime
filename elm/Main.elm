module Main exposing (..)

import Html.App exposing (programWithFlags)
import Html exposing (..)
import Html.Attributes exposing (..)
import Date exposing (Date)


main : Program Flag
main =
    programWithFlags
        { init = init
        , subscriptions = subscriptions
        , update = update
        , view = view
        }


type alias Flag =
    { ev : List DiaperEvent }


importEvent :
    List
        { id : Int
        , created_at : String
        , skipped_previous_feed : Maybe Bool
        , poop : Maybe Bool
        , pee : Maybe Bool
        , breast_feed : Maybe Int
        , bottle_feed : Maybe Int
        , attended_at : Maybe String
        , slept_at : Maybe String
        }


init : Flag -> ( Model, Cmd Msg )
init des =
    ( { newEvent = Nothing
      , events = List.map importEvent des.ev
      }
    , Cmd.none
    )


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none


type alias Model =
    { newEvent : Maybe DiaperEvent
    , events : List DiaperEvent
    }


type alias DiaperEvent =
    { id : Int
    , created_at : Date
    , skipped_previous_feed : Maybe Bool
    , poop : Maybe Bool
    , pee : Maybe Bool
    , breast_feed : Maybe Int
    , bottle_feed : Maybe Int
    , attended_at : Maybe Date
    , slept_at : Maybe Date
    }


type FieldChange
    = ChangeSkippedPrevious Bool
    | ChangePoop Bool
    | ChangePee Bool
    | ChangeBreastFeed Int
    | ChangeBottleFeed Int
    | ChangeAttended Date
    | ChangeSlept Date


type Msg
    = Entry FieldChange


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Entry fc ->
            case fc of
                ChangeSkippedPrevious sp ->
                    model ! []

                ChangePoop sp ->
                    model ! []

                ChangePee sp ->
                    model ! []

                ChangeBreastFeed sp ->
                    model ! []

                ChangeBottleFeed sp ->
                    model ! []

                ChangeAttended sp ->
                    model ! []

                ChangeSlept sp ->
                    model ! []


view : Model -> Html Msg
view model =
    div [] [ text <| toString <| model ]
