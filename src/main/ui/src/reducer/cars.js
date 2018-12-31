import {CREATED_CAR} from '../action/arrival';

const initialState = [];

export default function (state = initialState, action) {
  switch (action.type) {
    case CREATED_CAR:
      return [...state, action.payload.car];
    default:
      return state;
  }
}
