import {WEBSOCKET_CONNECT, WEBSOCKET_SEND} from '../middleware/websocket';

export const CREATE_TRACK = 'CREATE_TRACK';
export const CREATED_TRACK = 'CREATED_TRACK';
export const CREATED_CAR = 'CREATED_CAR';

const eventToActionAdapters = {
  CREATED_TRACK: ({payload}) => ({type: CREATED_TRACK, payload}),
  CREATED_CAR: ({payload}) => ({type: CREATED_CAR, payload}),
};

export function messageToActionAdapter(msg) {
  const event = JSON.parse(msg.data);

  if (eventToActionAdapters[event.type]) {
    return eventToActionAdapters[event.type](event);
  }
}

export function connectToArrivalServer(url) {
  return dispatch => {
    dispatch({type: WEBSOCKET_CONNECT, payload: {url}});
  }
}

function overTheSocket(type, payload) {
  return {
    type: WEBSOCKET_SEND,
    payload: {type, payload}
  };
}

export function sendTrack(track) {
  return dispatch => {
    dispatch(overTheSocket(CREATE_TRACK, {track}));
  }
}
