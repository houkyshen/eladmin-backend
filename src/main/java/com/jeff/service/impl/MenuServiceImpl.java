package com.jeff.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jeff.domain.Menu;
import com.jeff.domain.vo.MenuMetaVo;
import com.jeff.domain.vo.MenuVo;
import com.jeff.repository.MenuRepository;
import com.jeff.service.MenuService;
import com.jeff.service.RoleService;
import com.jeff.service.dto.MenuDto;
import com.jeff.service.dto.small.RoleSmallDto;
import com.jeff.service.mapstruct.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;
    private final RoleService roleService;

    /**
     * 获取用户能看到的菜单
     *
     * @param currentUserId /
     * @return
     */
    @Override
    public List<MenuDto> findByUser(Long currentUserId) {
        //获取当前登录用户的所有角色
        List<RoleSmallDto> roles = roleService.findByUsersId(currentUserId);
        //把角色集合转化成角色id集合
        Set<Long> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
        //根据角色查询能看到的菜单，并且菜单类型不能为2,类型为2的菜单是具体权限或者说是按钮，如用户新增、编辑、删除等；
        LinkedHashSet<Menu> menus = menuRepository.findByRoleIdsAndTypeNot(roleIds, 2);
        return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
    }


    /**
     * 构建菜单树
     *
     * @param menuDtos 用户能看到的菜单集合
     * @return
     */
    @Override
    public List<MenuDto> buildTree(List<MenuDto> menuDtos) {
        List<MenuDto> trees = new ArrayList<>();//顶层菜单的集合
        Set<Long> ids = new HashSet<>();//子菜单id的集合，避免重复所以使用Set而不是List
        for (MenuDto menuDTO : menuDtos) {//遍历用户能看到的每个菜单
            //如果没有上级菜单，代表是一级菜单，添加进trees里面
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (MenuDto child : menuDtos) {//再次遍历用户能看到的每个菜单
                //找出menuDTO的下级菜单
                if (menuDTO.getId().equals(child.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(child);
                    ids.add(child.getId());
                }
            }
        }
        if (trees.size() == 0) {//如果该用户一个顶级菜单都没权限看
            //在用户能看到的菜单集合里面筛选出所有非子菜单，意思就是一级菜单没有，就显示二级菜单，以此类推
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    /**
     * 完成从menuDTO到menuVO的转化
     *
     * @param menuDtos 已经是菜单树
     * @return
     */
    @Override
    public List<MenuVo> buildMenus(List<MenuDto> menuDtos) {
        List<MenuVo> trees = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(), menuDTO.getIcon(), !menuDTO.getCache()));
                        //处理子菜单
                        List<MenuDto> menuDtoList = menuDTO.getChildren();
                        if (CollectionUtil.isNotEmpty(menuDtoList)) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                        }
                        trees.add(menuVo);
                    }
                }
        );
        return trees;
    }
}
