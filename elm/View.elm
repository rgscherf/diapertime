module View exposing (..)

import Html exposing (..)
import Html.Attributes as Attributes exposing (style)
import Html.Events exposing (onInput, onClick)
import Types exposing (Model, Msg(..), DiaperEvent, FieldChange(..), TimeDelta(..))
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
    Scheme.topWithScheme Color.Teal Color.Purple <|
        Layout.render
            Mdl
            model.mdl
            [ Layout.fixedHeader ]
            { header =
                [ h1 [ style [ "font-family" => "'Lily Script One', cursive" ] ]
                    [ Icon.view "ic_alarm"
                        [ Icon.size48
                        , css "margin-left" "-100px"
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
        [ newEventButton model
        , cancelEventButton model
        , renderDiaperTimeTable model
        ]


renderNewEvent : Model -> Html Msg
renderNewEvent model =
    let
        blockInputStyle act =
            [ style
                [ "width" => "100px"
                , "height" => "30px"
                , "line-height" => "30px"
                , "border" => "1px solid black"
                , "border-radius" => "5px"
                , "display" => "inline-block"
                , "background-color" => "pink"
                , "text-align" => "center"
                , "margin" => "2px"
                ]
            , onClick act
            ]
    in
        div
            [ style
                [ "border" => "1px solid black"
                , "width" => "559px"
                , "margin-left" => "auto"
                , "margin-right" => "auto"
                , "margin-bottom" => "30px"
                ]
            ]
            [ div []
                -- poop entry
                [ span [] [ text "Poop" ]
                , div
                    (blockInputStyle NoOp)
                    [ text "1" ]
                , div
                    (blockInputStyle NoOp)
                    [ text "1" ]
                ]
            , div []
                [ input
                    [ Attributes.placeholder "Poop", onInput (\a -> Entry (ChangePoop a)) ]
                    [ text << toString <| model.newEvent.poop ]
                ]
            , div []
                [ input
                    [ Attributes.placeholder "Breast", onInput (\a -> Entry (ChangeBreastFeed a)) ]
                    [ text << toString <| model.newEvent.poop ]
                ]
            , div []
                [ input
                    [ Attributes.placeholder "Bottle", onInput (\a -> Entry (ChangeBottleFeed a)) ]
                    [ text << toString <| model.newEvent.poop ]
                ]
            , Button.render Mdl
                [ 2 ]
                model.mdl
                [ Button.accent
                , Button.onClick CancelEvent
                ]
                [ Icon.i "add" ]
            ]


newEventButton : Model -> Html Msg
newEventButton model =
    let
        newEventIsActive =
            model.showNewEvent
    in
        Button.render Mdl
            [ 0 ]
            model.mdl
            [ Button.fab
            , Button.accent
            , Elevation.e8
            , Button.onClick
                (if newEventIsActive then
                    CancelEvent
                 else
                    ResetNewEvent
                )
            , css "margin-top" "30px"
            , css "margin-bottom" "-20px"
            , css "left" "77%"
            , css "z-index" "10"
            ]
            [ Icon.i
                (if newEventIsActive then
                    "send"
                 else
                    "add"
                )
            ]


cancelEventButton : Model -> Html Msg
cancelEventButton model =
    case model.showNewEvent of
        False ->
            span [] []

        True ->
            Button.render Mdl
                [ 4 ]
                model.mdl
                [ Button.icon
                , Elevation.e8
                , Button.onClick CancelEvent
                , css "margin-top" "30px"
                , css "margin-bottom" "-20px"
                , css "left" "57%"
                , css "z-index" "10"
                ]
                [ Icon.i "remove" ]


inputTableRow : Model -> Html Msg
inputTableRow model =
    let
        fieldAccessor f =
            case f of
                ChangeAttended _ ->
                    model.neweventDeltas.attended

                ChangeSlept _ ->
                    model.neweventDeltas.slept

                _ ->
                    Debug.crash "should't happen"

        inputButtonTemplate renderNum deltaField deltaValue deltaString =
            Button.render
                Mdl
                [ renderNum ]
                model.mdl
                (List.append
                    [ Button.onClick (Entry (deltaField deltaValue))
                    , css "width" "100px"
                    , css "padding-left" "0px"
                    ]
                    (if (fieldAccessor <| deltaField Now) == deltaValue then
                        [ Button.colored ]
                     else
                        []
                    )
                )
                [ text deltaString ]
    in
        if model.showNewEvent then
            Table.tr []
                [ Table.td
                    [ css "padding-left" "auto"
                    , css "padding-right" "auto"
                    ]
                    [ inputButtonTemplate 5 ChangeAttended Now "0m ago"
                    , br [] []
                    , inputButtonTemplate 6 ChangeAttended Minus15 "15m ago"
                    , br [] []
                    , inputButtonTemplate 7 ChangeAttended Minus30 "30m ago"
                    , br [] []
                    , inputButtonTemplate 8 ChangeAttended Minus45 "45m ago"
                    , br [] []
                    , inputButtonTemplate 9 ChangeAttended Minus60 "60m ago"
                    , br [] []
                    , inputButtonTemplate 10 ChangeAttended Minus90 "90m ago"
                    , br [] []
                    , inputButtonTemplate 11 ChangeAttended Minus120 "120m ago"
                    ]
                , Table.td [] []
                , Table.td [] []
                , Table.td [] []
                , Table.td [] []
                , Table.td []
                    [ inputButtonTemplate 12 ChangeSlept Now "0m ago"
                    , br [] []
                    , inputButtonTemplate 13 ChangeSlept Minus15 "15m ago"
                    , br [] []
                    , inputButtonTemplate 14 ChangeSlept Minus30 "30m ago"
                    , br [] []
                    , inputButtonTemplate 15 ChangeSlept Minus45 "45m ago"
                    , br [] []
                    , inputButtonTemplate 16 ChangeSlept Minus60 "60m ago"
                    , br [] []
                    , inputButtonTemplate 17 ChangeSlept Minus90 "90m ago"
                    , br [] []
                    , inputButtonTemplate 18 ChangeSlept Minus120 "120m ago"
                    ]
                ]
        else
            div [] []


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
        , Table.tbody [] ([ inputTableRow model ] ++ List.map renderSingleRow model.events)
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
