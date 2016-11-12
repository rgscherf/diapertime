module View exposing (..)

import Html exposing (..)
import Html.Attributes exposing (style)
import Types exposing (Model, Msg(..), DiaperEvent)
import Material.Elevation as Elevation
import Material.Button as Button
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
            { header =
                [ h1 [ style [ "font-family" => "'Lily Script One', cursive" ] ]
                    [ Icon.view "ic_alarm"
                        [ Icon.size48
                        , css "margin-left" "-50px"
                        , css "vertical-align" "middle"
                        , css "padding-bottom" "10px"
                        , css "margin-right" "20px"
                        ]
                    , text "Diaper Time"
                    ]
                ]
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
        [ style
            [ "margin-left" => "auto"
            , "margin-right" => "auto"
            , "width" => "85%"
            , "max-width" => "800px"
            ]
        ]
        [ renderNewEvent model
        , renderDiaperTimeTable model
        ]


renderNewEvent : Model -> Html Msg
renderNewEvent model =
    Button.render Mdl
        [ 0 ]
        model.mdl
        [ Button.fab
        , Button.accent
        , Elevation.e8
        , css "margin-top" "30px"
        , css "margin-bottom" "-20px"
        , css "left" "77%"
        , css "z-index" "10"
        ]
        [ Icon.i "add" ]


renderDiaperTimeTable : Model -> Html Msg
renderDiaperTimeTable model =
    Table.table
        [ css "margin-right" "auto"
        , css "margin-left" "auto"
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
renderFeedCell numberToDisplay unitStringToAppend =
    let
        op =
            case numberToDisplay of
                0 ->
                    opFaded

                _ ->
                    opFull
    in
        Table.td
            [ css "opacity" op, css "text-align" "right" ]
            [ toString numberToDisplay
                |> (\m -> m ++ " " ++ unitStringToAppend)
                |> text
            ]


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
renderCheckCell isChecked =
    Table.td
        [ css "opacity" <|
            (if isChecked then
                opFull
             else
                opFaded
            )
        ]
        [ Icon.view
            (if isChecked then
                "ic_done"
             else
                "ic_clear"
            )
            [ Icon.size18
            , css "margin-left" "-50px"
            ]
        ]
