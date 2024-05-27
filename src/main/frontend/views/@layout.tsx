import {AppLayout} from '@vaadin/react-components/AppLayout.js';
import {DrawerToggle} from '@vaadin/react-components/DrawerToggle.js';
import {useRouteMetadata} from 'Frontend/util/routing.js';
import {Suspense, useEffect} from 'react';
import {Outlet, useLocation, useNavigate} from 'react-router-dom';
import {Icon, ProgressBar, SideNav, SideNavItem} from "@vaadin/react-components";
import {createMenuItems} from "@vaadin/hilla-file-router/runtime.js";

const navLinkClasses = ({isActive}: any) => {
    return `block rounded-m p-s ${isActive ? 'bg-primary-10 text-primary' : 'text-body'}`;
};


export default function MainLayout() {
    const navigate = useNavigate();
    const location = useLocation();

    const currentTitle = useRouteMetadata()?.title ?? 'My App';
    useEffect(() => {
        document.title = currentTitle;
    }, [currentTitle]);


    return (
        <AppLayout primarySection="drawer">
            <div slot="drawer" className="flex flex-col justify-between h-full p-m">
                <header className="flex flex-col gap-m">
                    <h1 className="text-l m-0">My application</h1>
                    <SideNav onNavigate={({path}) => path && navigate(path)} location={location}>
                        {
                            createMenuItems()
                                .filter(({to, icon, title}) => to?.endsWith("View"))
                                .map((menuItem) => {
                                    const {to, icon, title} = menuItem;

                                    return (
                                        <SideNavItem path={to} key={to}>
                                            {icon && <Icon icon={icon} slot={"prefix"}/>}
                                            {title}
                                        </SideNavItem>
                                    );
                                })
                        }
                    </SideNav>
                </header>
            </div>

            <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
            <h1 slot="navbar" className="text-l m-0">
                {currentTitle}
            </h1>

            <Suspense fallback={<ProgressBar indeterminate={true} className="m-0"/>}>
                <Outlet/>
            </Suspense>
        </AppLayout>
    );
}
