import PropTypes from 'prop-types';

const formatDate = (date) => {
    let day = date.getDate();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();
    return `${year}-${month<10?`0${month}`:`${month}`}-${day<10?`0${day}`:`${day}`}`;
}

const getNextDays = (numDays) => {
    const days = [];
    const date = new Date();
    for (let i = 0; i < numDays; i++) {
        days.push(new Date(date.getTime()));
        date.setDate(date.getDate() + 1);
    }
    return days;
}

const DateSelector = (props) => {
    const days = getNextDays(7);

    return (
        <select className="custom-select" {...props}>
            {days.map(date => {
                const dateString = formatDate(date);
                return <option key={dateString} value={dateString}>
                    {date.toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
                </option>;
            })}
        </select>
    );
}

DateSelector.propTypes = {
    id: PropTypes.string,
    className: PropTypes.string,
    value: PropTypes.string,
    onChange: PropTypes.func
};

export default DateSelector;
