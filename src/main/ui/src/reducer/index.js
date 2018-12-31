import { combineReducers } from 'redux';
import TracksReducer from './tracks';
import CarsReducer from './cars';

const rootReducer = combineReducers({
  tracks: TracksReducer,
  cars: CarsReducer,
});

export default rootReducer;
