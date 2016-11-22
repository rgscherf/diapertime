module Update exposing (..)

import Types exposing (Model, Msg(..), FieldChange(..), freshDiaperEvent)
import Material
import String


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        Mdl m ->
            Material.update m model

        ResetNewEvent ->
            { model
                | newEvent = freshDiaperEvent
                , showNewEvent = True
            }
                ! []

        NoOp ->
            model ! []

        CancelEvent ->
            { model
                | newEvent = freshDiaperEvent
                , showNewEvent = False
            }
                ! []

        Entry fc ->
            case fc of
                ChangeAttended sp ->
                    { model | neweventDeltas = { attended = sp, slept = model.neweventDeltas.slept } } ! []

                ChangeSkippedPrevious sp ->
                    model ! []

                ChangePoop sp ->
                    let
                        newPoop =
                            intWithDefault model.newEvent.poop sp

                        ev =
                            model.newEvent
                    in
                        { model | newEvent = { ev | poop = newPoop } } ! []

                ChangePee sp ->
                    model ! []

                ChangeBreastFeed sp ->
                    let
                        newBreast =
                            intWithDefault model.newEvent.breastFeed sp

                        ev =
                            model.newEvent
                    in
                        { model | newEvent = { ev | breastFeed = newBreast } } ! []

                ChangeBottleFeed sp ->
                    let
                        newBottle =
                            intWithDefault model.newEvent.breastFeed sp

                        ev =
                            model.newEvent
                    in
                        { model | newEvent = { ev | bottleFeed = newBottle } } ! []

                ChangeSlept sp ->
                    { model | neweventDeltas = { attended = model.neweventDeltas.attended, slept = sp } } ! []


intWithDefault : Int -> String -> Int
intWithDefault defaultInt stringToConvert =
    case String.toInt stringToConvert of
        Ok i ->
            i

        Err _ ->
            defaultInt
