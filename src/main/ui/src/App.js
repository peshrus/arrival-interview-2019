import React, {Component} from "react";
import {connect} from 'react-redux';
import ReactTable from "react-table";
import "react-table/react-table.css";
import {connectToArrivalServer, sendTrack} from './action/arrival';
import "./App.css";
import TrackCarsAdd from "./component/TrackCarsAdd";

const EMPTY_CAR = {
  code: '',
  transmission: 'automatic',
  ai: 'disabled',
  maxSpeed: {
    unit: 'mps',
    value: ''
  }
};

const EMPTY_TRACK = {
  name: '',
  description: '',
  length: {
    unit: 'km',
    value: ''
  },
  cars: [
    JSON.parse(JSON.stringify(EMPTY_CAR))
  ]
};

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      newTrack: JSON.parse(JSON.stringify(EMPTY_TRACK))
    };
  }

  handleAddTrackWithCars = () => {
    this.props.sendTrack(this.state.newTrack);

    this.setState({
      newTrack: JSON.parse(JSON.stringify(EMPTY_TRACK)),
    });
  };

  handleAddCar = () => {
    this.state.newTrack.cars.push(JSON.parse(JSON.stringify(EMPTY_CAR)));
    this.setState({newTrack: this.state.newTrack});
  };

  handleRemoveCar = index => {
    this.state.newTrack.cars.splice(index, 1);
    this.setState({newTrack: this.state.newTrack});
  };

  handleTrackChange = event => {
    let track = this.state.newTrack;
    let formEl = event.target.name;

    if (formEl.indexOf('length') >= 0) {
      if (formEl === 'length') {
        track.length.value = event.target.value;
      } else {
        track.length.unit = event.target.value;
      }
    } else {
      track[formEl] = event.target.value
    }

    this.setState({newTrack: track});
  };

  handleCarChange = index => event => {
    let car = this.state.newTrack.cars[index];
    let formEl = event.target.name;
    let value = event.target.value;

    if (formEl.indexOf('maxSpeed') >= 0) {
      if (formEl === 'maxSpeed') {
        car.maxSpeed.value = value;
      } else {
        car.maxSpeed.unit = value;
      }
    } else if (formEl === 'ai') {
      car[formEl] = event.target.checked ? 'enabled' : 'disabled'
    } else {
      car[formEl] = value
    }

    this.setState({newTrack: this.state.newTrack});
  };

  render() {
    const {tracks, cars} = this.props;
    return (
        <div className="App">
          <TrackCarsAdd handleAddTrackWithCars={this.handleAddTrackWithCars}
                        newTrack={this.state.newTrack}
                        handleAddCar={this.handleAddCar}
                        handleRemoveCar={this.handleRemoveCar}
                        handleTrackChange={this.handleTrackChange}
                        handleCarChange={this.handleCarChange}/>

          <div>
            <ReactTable
                data={tracks}
                columns={[
                  {
                    Header: "ID",
                    accessor: "id",
                    show: false,
                  },
                  {
                    Header: "Track Name",
                    accessor: "name",
                  },
                  {
                    id: "lengthValue",
                    Header: "Length Value",
                    accessor: d => d.length.value,
                  },
                  {
                    id: "lengthUnit",
                    Header: "Length Unit",
                    accessor: d => d.length.unit,
                  },
                  {
                    Header: "Description",
                    accessor: "description",
                  }
                ]}
                defaultPageSize={10}
                className="-striped -highlight"
                SubComponent={row => {
                  return (
                      <div className="p-3 bg-secondary">
                        <ReactTable
                            data={cars.filter(
                                car => car.trackId === row.original.id)}
                            columns={[
                              {
                                Header: "Car Code",
                                accessor: "code",
                              },
                              {
                                Header: "Transmission",
                                accessor: "transmission",
                              },
                              {
                                Header: "AI",
                                accessor: "ai",
                              },
                              {
                                id: "maxSpeedValue",
                                Header: "Max Speed Value",
                                accessor: d => d.maxSpeed.value,
                              },
                              {
                                id: "maxSpeedUnit",
                                Header: "Max Speed Unit",
                                accessor: d => d.maxSpeed.unit,
                              }
                            ]}
                            defaultPageSize={3}
                            showPageSizeOptions={false}
                            className="-striped -highlight bg-light"
                        />
                      </div>
                  );
                }}
            />
          </div>
        </div>
    );
  }

  componentDidMount() {
    this.props.connectToArrivalServer(
        `ws://${location.host}/websocket/arrival`);
  }
}

function mapStateToProps({tracks, cars}) {
  return {tracks, cars};
}

export default connect(mapStateToProps, {connectToArrivalServer, sendTrack})(
    App);