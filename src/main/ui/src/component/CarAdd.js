import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Grid from '@material-ui/core/Grid';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import {withStyles} from '@material-ui/core/styles';
import Switch from '@material-ui/core/Switch';
import PropTypes from 'prop-types';
import React, {Component} from 'react';
import {TextValidator} from 'react-material-ui-form-validator';

const CarsAddRows = props => {
  const {classes} = props;

  return props.newCars.map((newCar, index) => {
    let buttonOnClick = () => props.handleRemoveCar(index);
    let label = '-';

    if (index === 0) {
      buttonOnClick = props.handleAddCar;
      label = '+';
    }

    let button = <Button id={'addRemoveCarButton-' + index}
                         name="addRemoveCarButton" variant="contained"
                         className={classes.formControl + ' '
                         + classes.addRemoveCarButton}
                         onClick={buttonOnClick}>{label} Car</Button>;

    return (
        <Grid item xs={12} key={index}>
          <Grid container justify="center">
            <TextValidator
                id={'code-' + index}
                name="code"
                label="Car Code *"
                placeholder="e.g. rdb1"
                InputLabelProps={{shrink: true,}}
                className={classes.formControl + ' ' + classes.code}
                value={newCar.code}
                onChange={props.handleCarChange(index)}
                validators={['required']}
                errorMessages={['required']}
                margin="normal"
            />

            <FormControl required className={classes.formControl + ' '
            + classes.transmission}>
              <InputLabel shrink={true}
                          htmlFor={'transmission-'
                          + index}>Transmission</InputLabel>
              <Select
                  value={newCar.transmission}
                  onChange={props.handleCarChange(index)}
                  onLoad={props.handleCarChange(index)}
                  inputProps={{
                    name: 'transmission',
                    id: 'transmission-' + index
                  }}>
                <MenuItem value="automatic">automatic</MenuItem>
              </Select>
            </FormControl>

            <FormControlLabel
                control={
                  <Switch
                      id={'ai-' + index}
                      name="ai"
                      onChange={props.handleCarChange(index)}
                      checked={newCar.ai === 'enabled'}
                      color="primary"
                  />
                }
                label="AI"
                className={classes.formControl + ' ' + classes.ai}
            />

            <TextValidator
                type="number"
                id={'maxSpeed-' + index}
                name="maxSpeed"
                label="Max Speed *"
                placeholder="e.g. 110.12121212"
                InputLabelProps={{shrink: true,}}
                className={classes.formControl + ' ' + classes.maxSpeed}
                value={newCar.maxSpeed.value}
                onChange={props.handleCarChange(index)}
                validators={['required', 'minNumber:1',
                  'matchRegexp:^[0-9.]+$']}
                errorMessages={['required', 'enter n >= 1']}
                margin="normal"
            />
            <FormControl
                className={classes.formControl + ' ' + classes.maxSpeedUnit}>
              <Select
                  value={newCar.maxSpeed.unit}
                  onChange={props.handleCarChange(index)}
                  onLoad={props.handleCarChange(index)}
                  inputProps={{
                    name: 'maxSpeedUnit',
                    id: 'maxSpeedUnit-' + index
                  }}>
                <MenuItem value="mps">mps</MenuItem>
              </Select>
            </FormControl>

            {button}
          </Grid>
        </Grid>
    );
  });
};

class CarAdd extends Component {
  render() {
    return (
        <CarsAddRows newCars={this.props.newCars}
                     handleAddCar={this.props.handleAddCar}
                     handleRemoveCar={this.props.handleRemoveCar}
                     handleCarChange={this.props.handleCarChange}
                     classes={this.props.classes}/>
    );
  }
}

CarAdd.propTypes = {
  classes: PropTypes.object.isRequired,
};

const styles = theme => ({
  formControl: {
    margin: theme.spacing.unit,
  },
  code: {
    width: 95,
  },
  transmission: {
    width: 115,
  },
  ai: {
    marginTop: 17,
  },
  maxSpeed: {
    width: 140,
  },
  maxSpeedUnit: {
    marginTop: 23,
  },
  addRemoveCarButton: {
    marginTop: 20,
    marginBottom: 20,
  },
});

export default withStyles(styles)(CarAdd);