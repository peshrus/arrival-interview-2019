import {withStyles} from '@material-ui/core';
import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import TextField from '@material-ui/core/TextField';
import PropTypes from 'prop-types';
import React, {Component} from 'react';
import {TextValidator, ValidatorForm} from 'react-material-ui-form-validator';
import CarAdd from './CarAdd';

class TrackCarsAdd extends Component {
  handleSubmit = event => {
    event.preventDefault();
    event.stopPropagation();

    this.props.handleAddTrackWithCars();
  };

  render() {
    const {classes} = this.props;

    return (
        <ValidatorForm id="newTrackWithCarsForm"
                       className={classes.container + ' mb-3'}
                       ref="form" onSubmit={this.handleSubmit}
                       onError={errors => console.log(errors)}>
          <Grid container className={classes.root}>
            <Grid item xs={12}>
              <Grid container justify="center">
                <TextValidator
                    id="name"
                    name="name"
                    label="Track Name *"
                    placeholder="e.g. Millbrook"
                    InputLabelProps={{shrink: true,}}
                    className={classes.formControl}
                    value={this.props.newTrack.name}
                    onChange={this.props.handleTrackChange}
                    validators={['required']}
                    errorMessages={['required']}
                    margin="normal"
                />

                <TextValidator
                    type="number"
                    id="length"
                    name="length"
                    label="Length *"
                    placeholder="e.g. 7.4"
                    InputLabelProps={{shrink: true,}}
                    className={classes.formControl + ' ' + classes.length}
                    value={this.props.newTrack.length.value}
                    onChange={this.props.handleTrackChange}
                    validators={['required', 'minNumber:1',
                      'matchRegexp:^[0-9.]$']}
                    errorMessages={['required', 'enter n >= 1']}
                    margin="normal"
                />

                <FormControl
                    className={classes.formControl + ' ' + classes.lengthUnit}>
                  <Select
                      value={this.props.newTrack.length.unit}
                      onChange={this.props.handleTrackChange}
                      onLoad={this.props.handleTrackChange}
                      inputProps={{
                        name: 'lengthUnit',
                        id: 'lengthUnit',
                      }}>
                    <MenuItem value="km">km</MenuItem>
                  </Select>
                </FormControl>

                <TextField
                    id="description"
                    name="description"
                    label="Description"
                    placeholder="e.g. Millbrook city course race track"
                    InputLabelProps={{shrink: true,}}
                    multiline
                    rows={1}
                    className={classes.formControl + ' ' + classes.description}
                    value={this.props.newTrack.description}
                    onChange={this.props.handleTrackChange}
                    margin="normal"
                />
              </Grid>
            </Grid>

            <CarAdd newCars={this.props.newTrack.cars}
                    handleAddCar={this.props.handleAddCar}
                    handleRemoveCar={this.props.handleRemoveCar}
                    handleCarChange={this.props.handleCarChange}/>

            <Grid item xs={12}>
              <Grid container justify="center">
                <Button id="submitButton" variant="contained" color="primary"
                        type="submit" className={classes.formControl + ' '
                + classes.submitButton}>Add Track</Button>
              </Grid>
            </Grid>
          </Grid>
        </ValidatorForm>
    );
  }
}

TrackCarsAdd.propTypes = {
  classes: PropTypes.object.isRequired,
};

const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  formControl: {
    margin: theme.spacing.unit,
  },
  length: {
    width: 70,
  },
  lengthUnit: {
    marginTop: 23,
  },
  description: {
    width: 280,
  },
  submitButton: {
    width: '50%'
  }
});

export default withStyles(styles)(TrackCarsAdd);