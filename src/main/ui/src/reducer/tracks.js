import {CREATED_TRACK} from '../action/arrival';

const initialState = [];

export default function (state = initialState, action) {
  switch (action.type) {
    case CREATED_TRACK:
      return [...state, action.payload.track];
    default:
      return state;
  }
}
