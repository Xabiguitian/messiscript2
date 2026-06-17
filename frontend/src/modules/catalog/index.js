import * as actions from './actions';
import reducer from './reducer';
import * as selectors from './selectors';

export {default as Billboard} from './components/Billboard';
export {default as MovieDetails} from './components/MovieDetails';
export {default as SessionDetails} from './components/SessionDetails';

export default {actions, reducer, selectors};
