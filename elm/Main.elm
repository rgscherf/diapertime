module Main exposing (..)

import Html.App exposing (programWithFlags)
import Html exposing (..)
import Date exposing (Date)
import Date.Extra exposing (Interval(..))
import Json.Decode as Decode exposing ((:=), int, string, bool)
import Json.Decode.Extra as DecodeExtra exposing (date)
import Task
import Time


main : Program String
main =
    programWithFlags
        { init = init
        , subscriptions = subscriptions
        , update = update
        , view = view
        }


init : String -> ( Model, Cmd Msg )
init diaperEventsJson =
    let
        getEventList s =
            case Decode.decodeString eventDecoder s of
                Ok res ->
                    res

                Err _ ->
                    []
    in
        ( { newEvent = Nothing
          , events = getEventList diaperEventsJson
          , currentDate = Nothing
          }
        , Task.perform (\_ -> NoOp) InitializeDate Date.now
        )


eventDecoder : Decode.Decoder (List DiaperEvent)
eventDecoder =
    Decode.list
        (Decode.object8
            DiaperEvent
            ("id" := int)
            ("created_at" := DecodeExtra.date)
            ("skipped_previous_feed" := bool)
            (Decode.maybe ("poop" := bool))
            (Decode.maybe ("breast_feed" := int))
            (Decode.maybe ("bottle_feed" := int))
            (Decode.maybe ("attended_at" := DecodeExtra.date))
            (Decode.maybe ("slept_at" := DecodeExtra.date))
        )


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.batch [ Time.every Time.minute (\_ -> IncrementDate) ]


type alias Model =
    { newEvent : Maybe DiaperEvent
    , events : List DiaperEvent
    , currentDate : Maybe Date
    }


type alias DiaperEvent =
    { id : Int
    , created_at : Date
    , skipped_previous_feed : Bool
    , poop : Maybe Bool
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
    | InitializeDate Date
    | IncrementDate
    | NoOp


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NoOp ->
            model ! []

        InitializeDate date ->
            { model | currentDate = Just date } ! []

        IncrementDate ->
            case model.currentDate of
                Nothing ->
                    model ! []

                Just d ->
                    { model | currentDate = Just (Date.Extra.add Minute 1 d) } ! []

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
