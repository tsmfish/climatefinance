/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ClimatefinanceTestModule } from '../../../test.module';
import { SectorDetailComponent } from '../../../../../../main/webapp/app/entities/sector/sector-detail.component';
import { SectorService } from '../../../../../../main/webapp/app/entities/sector/sector.service';
import { Sector } from '../../../../../../main/webapp/app/entities/sector/sector.model';

describe('Component Tests', () => {

    describe('Sector Management Detail Component', () => {
        let comp: SectorDetailComponent;
        let fixture: ComponentFixture<SectorDetailComponent>;
        let service: SectorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [SectorDetailComponent],
                providers: [
                    SectorService
                ]
            })
            .overrideTemplate(SectorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SectorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SectorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Sector(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.sector).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
