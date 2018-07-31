import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Disbursement } from './disbursement.model';
import { DisbursementPopupService } from './disbursement-popup.service';
import { DisbursementService } from './disbursement.service';
import { Project, ProjectService } from '../project';

@Component({
    selector: 'jhi-disbursement-dialog',
    templateUrl: './disbursement-dialog.component.html'
})
export class DisbursementDialogComponent implements OnInit {

    disbursement: Disbursement;
    isSaving: boolean;

    projects: Project[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private disbursementService: DisbursementService,
        private projectService: ProjectService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.projectService.query()
            .subscribe((res: HttpResponse<Project[]>) => { this.projects = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.disbursement.id !== undefined) {
            this.subscribeToSaveResponse(
                this.disbursementService.update(this.disbursement));
        } else {
            this.subscribeToSaveResponse(
                this.disbursementService.create(this.disbursement));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Disbursement>>) {
        result.subscribe((res: HttpResponse<Disbursement>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Disbursement) {
        this.eventManager.broadcast({ name: 'disbursementListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProjectById(index: number, item: Project) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-disbursement-popup',
    template: ''
})
export class DisbursementPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private disbursementPopupService: DisbursementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.disbursementPopupService
                    .open(DisbursementDialogComponent as Component, params['id']);
            } else {
                this.disbursementPopupService
                    .open(DisbursementDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
