import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import {render} from 'react-dom';
import {AppContainer} from 'react-hot-loader';
import {Provider} from 'react-redux';
import {applyMiddleware, compose, createStore} from 'redux';
import reduxThunk from 'redux-thunk';
import '../public/favicon.ico'
import {messageToActionAdapter} from './action/arrival';
import App from './App';
import './index.css';
import websocket from './middleware/websocket';
import reducers from './reducer';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
const store = composeEnhancers(applyMiddleware(
    reduxThunk,
    websocket({messageToActionAdapter})
))(createStore)(reducers);

const renderApp = (Component) => {
  render(
      <Provider store={store}>
        <AppContainer>
          <Component/>
        </AppContainer>
      </Provider>
      , document.querySelector("#root")
  );
};

renderApp(App);

if (module && module.hot) {
  module.hot.accept('./App', () => {
    const NextRootContainer = require('./App');
    renderApp(NextRootContainer);
  });

  module.hot.accept('./reducer', () => {
    const nextReducer = require('./reducer/index').default;
    store.replaceReducer(nextReducer);
  });
}