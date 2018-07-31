/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DetailedSectorDeleteDialogComponent } from 'app/entities/detailed-sector/detailed-sector-delete-dialog.component';
import { DetailedSectorService } from 'app/entities/detailed-sector/detailed-sector.service';

describe('Component Tests', () => {
    describe('DetailedSector Management Delete Component', () => {
        let comp: DetailedSectorDeleteDialogComponent;
        let fixture: ComponentFixture<DetailedSectorDeleteDialogComponent>;
        let service: DetailedSectorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DetailedSectorDeleteDialogComponent]
            })
                .overrideTemplate(DetailedSectorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DetailedSectorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DetailedSectorService);
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
