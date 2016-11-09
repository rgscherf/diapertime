module View exposing (..)

import Html exposing (..)
import Types exposing (Model, Msg, DiaperEvent)


view : Model -> Html Msg
view model =
    div []
        [ table
            []
            [ tr []
                [ th [] [ text "Attended" ]
                , th [] [ text "Skipped?" ]
                , th [] [ text "Poop" ]
                , th [] [ text "Pee" ]
                , th [] [ text "Breast" ]
                , th [] [ text "Bottle" ]
                , th [] [ text "Slept" ]
                ]
            , tbody []
                (List.map
                    renderSingleRow
                    model.events
                )
            ]
        ]


renderSingleRow : DiaperEvent -> Html Msg
renderSingleRow event =
    let
        makeText =
            text << toString
    in
        tr []
            [ td [] [ makeText event.attendedAt ]
            , td [] [ makeText event.skippedPrevious ]
            , td [] [ makeText event.poop ]
            , td [] [ makeText event.pee ]
            , td [] [ makeText event.breastFeed ]
            , td [] [ makeText event.bottleFeed ]
            , td []
                [ makeText <|
                    Maybe.withDefault "" <|
                        Maybe.map toString event.sleptAt
                ]
            ]
