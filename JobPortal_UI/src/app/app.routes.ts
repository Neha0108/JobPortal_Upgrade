import { Routes } from '@angular/router';
import { Login } from './home/login/login';
import { Register } from './home/register/register';
import { LandingPage } from './home/landing-page/landing-page';
import { Dashboard as CandidateDashboard } from './candidate/dashboard/dashboard';
import { Dashboard } from './retailer/dashboard/dashboard';
import { Job } from './home/job/job';

export const routes: Routes = [

    { path: '', component: LandingPage },
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    { path: 'recruiter',component: Dashboard},
    { path:'jobs',component:Job},
    { path:'candidate',component: CandidateDashboard},

];
