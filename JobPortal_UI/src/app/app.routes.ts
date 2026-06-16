import { Routes } from '@angular/router';
import { Login } from '../home/login/login';
import { Register } from '../home/register/register';
import { LandingPage } from '../home/landing-page/landing-page';

export const routes: Routes = [

    { path: '', component: LandingPage },
    { path: 'login', component: Login },
    { path: 'register', component: Register }

];
