import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Disbursement } from 'app/shared/model/disbursement.model';
import { DisbursementService } from './disbursement.service';
import { DisbursementComponent } from './disbursement.component';
import { DisbursementDetailComponent } from './disbursement-detail.component';
import { DisbursementUpdateComponent } from './disbursement-update.component';
import { DisbursementDeletePopupComponent } from './disbursement-delete-dialog.component';
import { IDisbursement } from 'app/shared/model/disbursement.model';

@Injectable({ providedIn: 'root' })
export class DisbursementResolve implements Resolve<IDisbursement> {
    constructor(private service: DisbursementService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((disbursement: HttpResponse<Disbursement>) => disbursement.body));
        }
        return of(new Disbursement());
    }
}

export const disbursementRoute: Routes = [
    {
        path: 'disbursement',
        component: DisbursementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'disbursement/:id/view',
        component: DisbursementDetailComponent,
        resolve: {
            disbursement: DisbursementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'disbursement/new',
        component: DisbursementUpdateComponent,
        resolve: {
            disbursement: DisbursementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'disbursement/:id/edit',
        component: DisbursementUpdateComponent,
        resolve: {
            disbursement: DisbursementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const disbursementPopupRoute: Routes = [
    {
        path: 'disbursement/:id/delete',
        component: DisbursementDeletePopupComponent,
        resolve: {
            disbursement: DisbursementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Disbursements'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
