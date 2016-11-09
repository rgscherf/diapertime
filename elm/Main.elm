module Main exposing (..)

import Html.App exposing (programWithFlags)
import Date exposing (Date)
import Json.Decode as Decode exposing ((:=), int, string, bool, at)
import Update exposing (update)
import View exposing (view)
import Types exposing (Model, Msg, DiaperEvent)


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
