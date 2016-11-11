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
import Date.Format exposing (format)
import Date exposing (Date)


opFaded : String
opFaded =
    "0.38"


opFull : String
opFull =
    "1"


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
                , Table.th [] [ text "Breast" ]
                , Table.th [] [ text "Bottle" ]
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
            [ Table.td [] [ text <| renderDateText event.attendedAt ]
            , renderCheckCell event.pee
            , renderPoopCell event.poop
            , renderFeedCell event.breastFeed "min"
            , renderFeedCell event.bottleFeed "mL"
            , Table.td []
                [ text <|
                    Maybe.withDefault "" <|
                        Maybe.map renderDateText event.sleptAt
                ]
            ]


renderFeedCell : Int -> String -> Html Msg
renderFeedCell n app =
    let
        op =
            case n of
                0 ->
                    opFaded

                _ ->
                    opFull
    in
        Table.td
            [ css "opacity" op, css "text-align" "right" ]
            [ text << (\m -> m ++ " " ++ app) << toString <| n ]


renderDateText : Date -> String
renderDateText d =
    format "%k:%M%P %m/%d" d


renderPoopCell : Int -> Html Msg
renderPoopCell level =
    let
        -- strategy is to have [fullicons] ++ [fadedicons]
        makePoopIcons iconIsEmpty n =
            List.map
                (\_ ->
                    Icon.view
                        (if iconIsEmpty then
                            "ic_radio_button_unchecked"
                         else
                            "ic_lens"
                        )
                        [ Icon.size18
                        , css "opacity"
                            (if iconIsEmpty then
                                opFaded
                             else
                                opFull
                            )
                        , css "margin-left" "-50px"
                        ]
                )
                [0..n]

        fullIcons =
            -- [range] is inclusive of 0 on lower bound
            -- so if n == 0 we have to actually map
            -- [0..(-1)] for an empty mapped list
            makePoopIcons False (level - 1)

        emptyIcons =
            -- see note above!
            makePoopIcons True (2 - level)
    in
        Table.td
            []
        <|
            List.append
                fullIcons
                emptyIcons


renderSingleXCell : Html Msg
renderSingleXCell =
    Icon.view "ic_clear"
        [ Icon.size18
        , css "opacity" opFaded
        , css "margin-left" "-50px"
        ]


renderCheckCell : Bool -> Html Msg
renderCheckCell b =
    Table.td
        [ css "opacity" <|
            (if b then
                opFull
             else
                opFaded
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
