/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DisbursementDeleteDialogComponent } from 'app/entities/disbursement/disbursement-delete-dialog.component';
import { DisbursementService } from 'app/entities/disbursement/disbursement.service';

describe('Component Tests', () => {
    describe('Disbursement Management Delete Component', () => {
        let comp: DisbursementDeleteDialogComponent;
        let fixture: ComponentFixture<DisbursementDeleteDialogComponent>;
        let service: DisbursementService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DisbursementDeleteDialogComponent]
            })
                .overrideTemplate(DisbursementDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DisbursementDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DisbursementService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
