module Main exposing (..)

import Html.App exposing (programWithFlags)
import Html exposing (..)
import Date exposing (Date)
import Json.Decode as Decode exposing ((:=), int, string, bool, at)


main : Program String
main =
    programWithFlags
        { init = init
        , subscriptions = subscriptions
        , update = update
        , view = view
        }


init : String -> ( Model, Cmd Msg )
init diaperEventsFromJSON =
    let
        decodedDiaperEvents =
            case Decode.decodeString diaperEventsDecoder diaperEventsFromJSON of
                Ok res ->
                    res

                Err _ ->
                    []
    in
        ( { newEvent = Nothing
          , events = Debug.log "here" decodedDiaperEvents
          }
        , Cmd.none
        )


diaperEventsDecoder : Decode.Decoder (List DiaperEvent)
diaperEventsDecoder =
    Decode.list
        (Decode.object8
            DiaperEvent
            (at [ "_id", "$oid" ] string)
            (at [ "attendedAt", "$date" ] fromUnix)
            ("skippedPrevious" := bool)
            ("poop" := int)
            ("pee" := bool)
            ("breastFeed" := int)
            ("bottleFeed" := int)
            (Decode.maybe (at [ "sleptAt", "$date" ] fromUnix))
        )


fromUnix : Decode.Decoder Date.Date
fromUnix =
    let
        resultTime a =
            Ok <| Date.fromTime <| toFloat a
    in
        Decode.customDecoder int resultTime


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.batch []


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


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NoOp ->
            model ! []

        Entry fc ->
            case fc of
                ChangeAttended sp ->
                    model ! []

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

                ChangeSlept sp ->
                    model ! []


view : Model -> Html Msg
view model =
    div [] [ text <| toString <| model ]
