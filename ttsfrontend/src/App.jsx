import {useAuth} from './context/AuthContext';
import AuthPage from './pages/AuthPage';
import MainPage from './pages/MainPage';


function App(){const {
    isAuthenticated}=useAuth();
    return isAuthenticated ? <MainPage/> : <AuthPage/>;
    }

export default App;