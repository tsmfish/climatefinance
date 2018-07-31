/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DetailedSectorDetailComponent } from '../../../../../../main/webapp/app/entities/detailed-sector/detailed-sector-detail.component';
import { DetailedSectorService } from '../../../../../../main/webapp/app/entities/detailed-sector/detailed-sector.service';
import { DetailedSector } from '../../../../../../main/webapp/app/entities/detailed-sector/detailed-sector.model';

describe('Component Tests', () => {

    describe('DetailedSector Management Detail Component', () => {
        let comp: DetailedSectorDetailComponent;
        let fixture: ComponentFixture<DetailedSectorDetailComponent>;
        let service: DetailedSectorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DetailedSectorDetailComponent],
                providers: [
                    DetailedSectorService
                ]
            })
            .overrideTemplate(DetailedSectorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DetailedSectorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DetailedSectorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new DetailedSector(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.detailedSector).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
