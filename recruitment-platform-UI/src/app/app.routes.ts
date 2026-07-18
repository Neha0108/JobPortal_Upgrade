import { Routes } from '@angular/router';
import { Login } from './Auth/login/login';
import { Register } from './Auth/register/register';
import { ForgotPassword } from './Auth/forgot-password/forgot-password';
import { ResetPassword } from './Auth/reset-password/reset-password';
import { VerifyEmail } from './Auth/verify-email/verify-email';
import { Candidateprofile } from './Candidate/candidateprofile/candidateprofile';
import { Canidatedashboard } from './Candidate/canidatedashboard/canidatedashboard';
import { Candidatelayout } from './Candidate/candidatelayout/candidatelayout';
import { authGuard } from './gaurd/auth-guard';
import { roleGuard } from './gaurd/role-guard';
import { Role } from './models/role.enum';
import { Recruiterlayout } from './Recruiter/recruiterlayout/recruiterlayout';
import { Recruiterdashboard } from './Recruiter/recruiterdashboard/recruiterdashboard';
import { Recruiterprofile } from './Recruiter/recruiterprofile/recruiterprofile';
import { Company } from './company/company';
import { Candidatejob } from './Candidate/candidatejob/candidatejob';
import { Jobdetail } from './Candidate/jobdetail/jobdetail';
import { Applications } from './Candidate/applications/applications';
import { Savedjobs } from './Candidate/savedjobs/savedjobs';
import { RecruiterJob } from './Recruiter/recruiter-job/recruiter-job';
import { Jobform } from './Recruiter/jobform/jobform';
import { Jobapplicants } from './Recruiter/jobapplicants/jobapplicants';


export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'auth/login' },

  {
    path: 'auth',
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'login' },
      { path: 'login', component: Login },
      { path: 'register', component: Register },
      { path: 'forgot-password', component: ForgotPassword },
      { path: 'reset-password', component: ResetPassword },
      { path: 'verify-email', component: VerifyEmail },
    ],
  },

  {
    path: 'candidate',
    canActivate: [authGuard, roleGuard([Role.CANDIDATE])],
    component: Candidatelayout,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', component: Canidatedashboard },
      { path: 'profile', component: Candidateprofile },
      { path: 'jobs', component: Candidatejob},
      { path: 'jobs/:jobId', component: Jobdetail},
      { path: 'applications', component:Applications},
      { path: 'saved-jobs', component: Savedjobs}
    ],
  },
  {
    path: 'recruiter',
    canActivate: [authGuard, roleGuard([Role.RECRUITER])],
    component: Recruiterlayout,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', component: Recruiterdashboard },
      { path: 'profile', component: Recruiterprofile},
      { path: 'company', component: Company },
      { path: 'jobs', component: RecruiterJob},
      { path: 'jobs/new', component: Jobform},
      { path: 'jobs/:jobId/edit', component: Jobform},
      { path: 'jobs/:jobId/applicants', component: Jobapplicants}

      
    ],
  },

  { path: '**', redirectTo: 'auth/login' },
];