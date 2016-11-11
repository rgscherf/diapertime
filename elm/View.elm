module View exposing (..)

import Html exposing (..)
import Types exposing (Model, Msg(..), DiaperEvent)
import Material.Table as Table
import Material.Color as Color
import Material.Scheme as Scheme
import Material.Layout as Layout
import Material.Table as Table
import Material.Icon as Icon
import Material.Options exposing (css, cs)


view : Model -> Html Msg
view model =
    Scheme.topWithScheme Color.Teal Color.Indigo <|
        Layout.render
            Mdl
            model.mdl
            [ Layout.fixedHeader ]
            { header = [ h1 [] [ text "DTime" ] ]
            , drawer = []
            , tabs = ( [], [] )
            , main = [ viewBody model ]
            }


(=>) : a -> b -> ( a, b )
(=>) =
    (,)


viewBody : Model -> Html Msg
viewBody model =
    div
        []
        [ renderDiaperTimeTable model ]


renderDiaperTimeTable : Model -> Html Msg
renderDiaperTimeTable model =
    Table.table
        [ css "margin-left" "auto"
        , css "margin-right" "auto"
        , css "margin-top" "30px"
        ]
        [ Table.thead []
            [ Table.tr []
                [ Table.th [] [ text "Attended" ]
                , Table.th [] [ text "Pee" ]
                , Table.th [] [ text "Poop" ]
                , Table.th [] [ text "Breast min" ]
                , Table.th [] [ text "Bottle mL" ]
                , Table.th [] [ text "Slept" ]
                ]
            ]
        , Table.tbody [] (List.map renderSingleRow model.events)
        ]


renderSingleRow : DiaperEvent -> Html Msg
renderSingleRow event =
    let
        makeText =
            text << toString
    in
        Table.tr []
            [ Table.td [] [ makeText event.attendedAt ]
            , renderCheckCell event.pee
            , renderPoopCell event.poop
            , Table.td [] [ makeText event.breastFeed ]
            , Table.td [] [ makeText event.bottleFeed ]
            , Table.td []
                [ makeText <|
                    Maybe.withDefault "" <|
                        Maybe.map toString event.sleptAt
                ]
            ]


renderPoopCell : Int -> Html Msg
renderPoopCell level =
    Table.td
        [ cs "poopCell"
        ]
        (case level of
            0 ->
                [ renderSingleXCell ]

            l ->
                List.map
                    (\_ ->
                        Icon.view "ic_thumb_up"
                            [ Icon.size18
                            , css "opacity" "1"
                            , css "margin-left" "-50px"
                            ]
                    )
                    [0..l]
        )


renderSingleXCell : Html Msg
renderSingleXCell =
    Icon.view "ic_clear"
        [ Icon.size18
        , css "opacity" ".38"
        , css "margin-left" "-50px"
        ]


renderCheckCell : Bool -> Html Msg
renderCheckCell b =
    Table.td
        [ cs "check_cell"
        , css "opacity" <|
            (if b then
                "1"
             else
                ".38"
            )
        ]
        [ Icon.view
            (if b then
                "ic_done"
             else
                "ic_clear"
            )
            [ Icon.size18
            , css "margin-left" "-50px"
            ]
        ]
